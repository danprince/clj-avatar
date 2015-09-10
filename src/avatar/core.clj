(ns avatar.core
  (:gen-class))

(require '[avatar.bitmap :as bmp]
         '[avatar.image :as img])

(use 'ring.adapter.jetty
     'ring.middleware.file)

(def config {:static "/var/www/public"})

(defn avatar-handler [request]
  (let [uri (:uri request)
        hashed (hash uri)
        bitmap (bmp/create-bitmap hashed 6 6)
        image (img/bitmap->image 20 hashed bitmap)]
    (img/save-png image (format "%s%s" (:static config) uri))))

(def handler (wrap-file avatar-handler (:static config)))

(defn -main [& args]
  (run-jetty handler {:port 8080}))

