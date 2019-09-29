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

(defn in-p-tag [{:keys [ip name ping]}]
  [:p name "(" ip ")=" (str ping)])

(go (let [response (<! (http/get "/nodes"))]
      (reset! nodes (:body response))
      #_(pprint @nodes)))

(defn some-component []
  [:div {:id "list"}
   (into [:div {:id "underlist"}] (map in-p-tag @nodes))])

(defn mount-it []
  (r/render [some-component nodes]
            (.getElementById js/document "table")))

(mount-it)

