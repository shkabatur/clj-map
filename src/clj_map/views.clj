(ns clj-map.views
  (:use
    [hiccup.page :only [html5 include-css include-js]]))

#_(defn index [req]
  (html5 [:head
          [:meta {:charset "utf-8"}]]
         [:body
          [:div  {:style "position:absolute"}
           [:button  {:onclick "window.location='edit';"} "Edit"]
           [:button  {:onclick "window.location='table';"} "Table"]]
          [:canvas {:id "canvas"
                    :width "1911"
                    :height "860"}]
          [:script {:src "js/script.js"}]])
  )

(defn table [req]
     (html5 [:head
             [:meta {:charset "utf-8"}]]
            [:body
             [:div {:id "table"}]
             [:script {:src "js/table.js"}]]))

(defn index [req]
  (html5 [:head
          [:meta {:charset "utf-8"}]]
         [:body
          [:div  {:style "position:absolute"}
           [:button  {:onclick "window.location='edit';"} "Edit"]
           [:button  {:onclick "window.location='table';"} "Table"]
           [:div {:id "mount-point"}]
           ]

          [:canvas {:id "canvas"
                    :width "1911"
                    :height "860"}]
          [:script {:src "js/map.js"}]]))