(ns avatar.image
  (:gen-class))

(import 'java.awt.image.BufferedImage
        'javax.imageio.ImageIO
        'java.awt.Color
        'java.io.File)

(defn draw-row
  "Iterates over the row vector to draw the appropriate blocks onto
  the graphics object."
  [{bs :block-size g :graphics color :color :as opts} y row]
    (doall
      (map-indexed
        (fn [x bit]
          (doto g
            (.setColor (if (zero? bit)
                         Color/WHITE
                         color))
            (.fillRect (* x bs)
                       (* y bs)
                       bs
                       bs)))
      row)))

(defn draw-rows
  "Iterates over a vector of rows and draws them each"
  [opts rows]
    (doall
      (map-indexed
        (fn [y row]
          (draw-row opts y row))
        rows)))

(defn bitmap->image
  "Creates a buffered image from a bitmap."
  [block-size seed bitmap]
    (let [width    (count (first bitmap))
          height   (count bitmap)
          scaled-w (* width block-size)
          scaled-h (* height block-size)
          img-type BufferedImage/TYPE_INT_ARGB
          color    (Color. seed true)
          img      (BufferedImage. scaled-w scaled-h img-type)
          graphics (.getGraphics img)]
      (draw-rows
        { :block-size block-size
          :graphics graphics
          :color color }
        bitmap)
      img))

(defn save-png
  "Saves a buffered image to file."
  [img name]
    (print (str "Writing to " name))
    (ImageIO/write img "png" (File. name)))

