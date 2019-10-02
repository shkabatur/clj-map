(ns clj-map.core
  (:use [org.httpkit.server :only [run-server]]
        [cheshire.core :only [generate-string parse-string]]
        [compojure.route :only [files not-found resources]]
        [compojure.core :only [defroutes GET POST]]
        [overtone.at-at :only [every mk-pool]]
        [clj-map.ping :only [ping]]
        [clj-map.views :only [index table]]
        [buddy.auth.backends :only [basic]]
        [buddy.auth.middleware :only [wrap-authentication]]
        )
  (:gen-class))

(defonce server (atom nil))

(defn stop-server []
  (when-not (nil? @server)
    (@server :timeout 100)
    (reset! server nil)))

(defn start-server [app]
  (when (nil? @server)
    (reset! server (run-server app {:port 80}))))

(def my-pool (mk-pool))
(def nodes (atom []))
(reset! nodes (parse-string (slurp "nodes.json") true))


(def pinged-nodes (atom []))
;(reset! pinged-nodes (map conj @nodes (doall (pmap ping (map :ip @nodes)))))


(defn update-pinged-nodes
  [n]
  (every n
         #(reset! pinged-nodes (map conj @nodes (doall (pmap ping (map :ip @nodes)))))
         my-pool))

(defn my-authfn
  [req authdata]
  (let [username (:username authdata)
        password (:password authdata)]
    (= username password "admin")))

(def backend (basic {:realm "MyApi"
                     :authfn my-authfn}))

(defroutes app
           (GET "/" [] #'index)

           (GET "/edit" []
             (fn [req]
               (if (:identity req)
                 {:status  200
                  :headers {"Content-Type" "text/html"}
                  :body    (slurp "resources/html/editor.html")}

                 {:status 401
                  :headers {"WWW-Authenticate" "Basic"}
                  }
                 )
               ))

           (GET "/nodes" []
             (fn [req]
               {:status  200
                :headers {"Content-Type" "application/json"}
                :body    (generate-string @pinged-nodes)
                }))

           (GET "/table" [] #'table)

           (POST "/change-nodes" []
             (fn [req]
               (reset! nodes (parse-string (slurp (:body req)) true))
               (spit "nodes.json" (generate-string @nodes))))

           (resources "/")
           (not-found "<p>Page not found.</p>"))

(def app-with-auth
  (wrap-authentication #'app backend))

(defn -main
  [& args]
  (update-pinged-nodes 4000)
  (reset! server (run-server #'app-with-auth {:port 80}))
  (println "Server is up.... port - 80")
  )