(ns clj-map.core
  (:use [org.httpkit.server :only [run-server]]
        [cheshire.core :only [generate-string parse-string]]
        [compojure.route :only [files not-found resources]]
        [compojure.core :only [defroutes GET POST]]
        [clj-map.ping :only [ping]]
        )
  (:gen-class))


(defn ping-nodes [nodes]
  (let [pinged-nodes (map conj nodes (doall (pmap ping (map :ip nodes))))]
    (generate-string pinged-nodes)))

(defroutes app
  (GET "/" []
       (fn [req]
         {:status 200
          :headers {"Content-Type" "text/html"}
          :body (slurp "resources/html/index.html")}))

  (GET "/edit" []
       (fn [req]
         {:status 200
          :headers {"Content-Type" "text/html"}
          :body (slurp "resources/html/editor.html")}))

  (GET "/nodes" []
       (fn [req]
         {:status 200
          :headers {"Content-Type" "application/json"}
          :body (ping-nodes (parse-string (slurp "nodes.json")))
          }))

  (resources "/")
  (not-found "<p>Page not found.</p>"))


(defn -main
  [& args]
  (run-server app {:port 80})
)
