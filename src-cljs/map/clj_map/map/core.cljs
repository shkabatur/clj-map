(ns clj-map.map.core
            (:require-macros [cljs.core.async.macros :refer [go]])
            (:require [cljs-http.client :as http]
                      [cljs.core.async :refer [<!]]
                      #_[reagent.core :as r]
                      #_[cljs.pprint :refer [pprint]]
                      [clj-map.canvas.core :as canvas]
                      ))


(enable-console-print!)

(def nodes (atom []))
;--------------------------------------------------------------
;setting up some pictures
(def map-img (canvas/create-image "img/map-night.jpg"))
(def green-img (canvas/create-image "img/green.png"))
(def red-img (canvas/create-image "img/red.png"))

;---------------------------------------------------------------
;setup canvas
(def canvas-dom (.getElementById js/document "canvas"))
(def ctx (canvas/create-2d-context canvas-dom))



;---------------------------------------------------------------
(defn draw-nodes
  [nodes]
  (doseq [{:keys [x y name result]} nodes]
    (if result
      (canvas/draw-image ctx green-img {:x x :y y :w 10 :h 10})
      (canvas/draw-image ctx red-img {:x x :y y :w 10 :h 10}))))

(set! (.-onclick canvas-dom)
      (fn [event]
        (let [x (.-clientX event)
              y (.-clientY event)]
          (print (filter #(and (= x (:x %)) (= y (:y %)))
                         @nodes))
          (print x y))))


(set! (.-onload js/window)
      (fn []
        (print "KEK")
        (canvas/draw-image ctx map-img {:x 0 :y 0 :w 1910 :h 859})
        (js/setInterval
          (fn []
            (canvas/draw-image ctx map-img {:x 0 :y 0 :w 1910 :h 859})
            (go (let [response (<! (http/get "/nodes"))]
                  (reset! nodes (:body response))))
            (draw-nodes @nodes))

          3000)
        ))
