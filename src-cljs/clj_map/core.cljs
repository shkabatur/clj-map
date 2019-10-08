(ns clj-map.core
            (:require-macros [cljs.core.async.macros :refer [go]])
            (:require [cljs-http.client :as http]
                      [cljs.core.async :refer [<!]]
                      [reagent.core :as r]
                      [clj-map.components :as c]
                      [cljs.pprint :refer [pprint]]
                      [cognitect.transit :as t]))


(enable-console-print!)

(println "HEY!")

(def nodes (r/atom []))

(defn trtd [{:keys [ip name ping]}]
  [:tr
   [:td name]
   [:td ip]
   [:td (str ping)]])

(defn my-table [nodes]
  (into [:table {:id "nodes-table" }
   [:tr
    [:th "Имя"]
    [:th "IP"]
    [:th "Пинг"]]
   ]
        (map trtd @nodes)))


(go (let [response (<! (http/get "/nodes"))]
      (reset! nodes (:body response))
      #_(pprint @nodes)))


(defn some-component []
  [:div {:id "list"}
   (into [:div {:id "underlist"}] (map in-p-tag @nodes))])


(defn mount-it []
  (r/render [my-table nodes]
            (.getElementById js/document "table")))

(mount-it)

(def interval (.setInterval
                js/window
                (fn []
                  (go (let [response (<! (http/get "/nodes"))]
                        (reset! nodes (:body response))
                        #_(pprint @nodes)))
                  )))