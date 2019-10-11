(ns clj-map.canvas.core)


(defn create-image
  "create new image"
  [img-path]
  (let [img (js/Image.)]
    (set! (.-src img) img-path)
    img))


(defn create-2d-context
  "return new 2d context"
  [canvas-dom]
  (let [ctx (.getContext canvas-dom "2d")]
    ctx))

(defn draw-image
  "Draws the image onto the canvas at the given position.
   If a map of params is given, the number of entries is used to
   determine the underlying call to make."
  ([ctx img x y]
   (. ctx (drawImage img x y))
   ctx)
  ([ctx img {:keys [x y w h
                    sx sy sw sh dx dy dw dh] :as params}]
   (condp = (count params)
     2 (. ctx (drawImage img x y))
     4 (. ctx (drawImage img x y w h))
     8 (. ctx (drawImage img sx sy sw sh dx dy dw dh)))
   ctx))


