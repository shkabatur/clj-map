(ns clj-map.map.core
            (:require-macros [cljs.core.async.macros :refer [go]])
            (:require [cljs-http.client :as http]
                      [cljs.core.async :refer [<!]]
                      #_[reagent.core :as r]
                      #_[cljs.pprint :refer [pprint]]
                      [monet.canvas :as canvas]))


(enable-console-print!)

(def nodes (atom []))
;--------------------------------------------------------------
;setting up some pictures
(def map-img (js/Image.))
(set! (.-src map-img) "img/map-night.jpg")

(def green-img (js/Image.))
(set! (.-src green-img) "img/green.png")

(def red-img (js/Image.))
(set! (.-src red-img) "img/red.png")
;---------------------------------------------------------------
;setup canvas
(def canvas-dom (.getElementById js/document "canvas"))
(def context (canvas/init canvas-dom "2d"))
;---------------------------------------------------------------
(canvas/add-entity context :map
                   (canvas/entity {:x 0 :y 0 :w 1910 :h 859} ; val
                                  nil                       ; update function
                                  (fn [ctx val] ; draw function
                                    (canvas/draw-image ctx map-img val))))

(defn draw-nodes
  [nodes]
  (doseq [{:keys [x y ip result]} nodes]
    #_(println x y ip)
    (canvas/add-entity context (keyword ip)
                       (canvas/entity {:x x :y y :w 10 :h 10}
                                      nil
                                      (fn [ctx val]
                                        (if result
                                          (canvas/draw-image ctx green-img val)
                                          (canvas/draw-image ctx red-img val)))))))



(set! (.-onload js/window)
      (fn []
        (print "KEK")
        (js/setInterval
          (fn []
            (go (let [response (<! (http/get "/nodes"))]
                  (reset! nodes (:body response))
                  #_(print (:entities context))
                  (draw-nodes @nodes))))

          3000)
        ))




(draw-nodes @nodes)