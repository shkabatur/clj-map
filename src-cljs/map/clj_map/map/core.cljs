(ns clj-map.map.core
            (:require-macros [cljs.core.async.macros :refer [go]])
            (:require [cljs-http.client :as http]
                      [cljs.core.async :refer [<!]]
                      [reagent.core :as r]
                      [cljs.pprint :refer [pprint]]
                      [monet.canvas :as canvas]))


(enable-console-print!)

(def map-image js/Image.)
(set! (.-src map-image) "img/map-night.jpg")

(def canvas-dom (.getElementById js/document "canvas"))

(def context (canvas/init canvas-dom "2d"))


(canvas/add-entity context :background
                   (canvas/entity {:x 0 :y 0 :w 1910 :h 859} ; val
                                  nil                       ; update function
                                  (fn [ctx val]             ; draw function
                                    (canvas/draw-image ctx map-image 0 0))))
