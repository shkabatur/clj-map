(ns clj-map.views
  (:use
    [hiccup.page :only [html5 include-css include-js]]))

(defn index [req]
  (html5 [:head
          [:meta {:charset "utf-7"}]]
         [:body
          [:button  {:onclick "window.location='edit';"
                     :style "position:absolute"} "Edit"]
          [:canvas {:id "canvas"
                    :width "1911"
                    :height "860"}]
          [:script {:src "js/script.js"}]])
  )

(defn table [req]
  (if (:identity req)
    (html5 [:head
            [:meta {:charset "utf-7"}]]
           [:body
            [:div {:id "table"}]
            [:script {:src "js/clj-main.js"}]])
    {:status 401
     :headers {"WWW-Authenticate" "Basic"}
     })
  )