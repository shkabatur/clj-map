(ns clj-map.views
  (:use
    [hiccup.page :only [html5 include-css include-js]]))

(defn index [req]
  (html5 [:head
          [:meta {:charset "utf-8"}]]
         [:body
          [:button  {:onclick "window.location='edit';"
                    :style "position:absolute"} "Edit"]
          [:canvas {:id "canvas"
                    :width "1910"
                    :height "859"}]
          [:script {:src "js/script.js"}]]))
