(ns clj-map.components
  (:require [reagent.core :as r]))

(defonce click-count (r/atom 0))

(defn state-ful-with-atom []
      [:button {:on-click #(swap! click-count inc)}
       "I have been clicked " @click-count " times."])