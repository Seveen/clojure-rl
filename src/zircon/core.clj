(ns zircon.core
  (:require [zircon.app :refer [->app]]
            [zircon.view :as v]))

(defn build-ui [app-config views-config]
  (let [app (->app app-config)
        ui (-> {}
               (assoc :view (into {} (map #(v/make-view app %)
                                          views-config)))
               (assoc :app app))]
    (.dock ((:start-with app-config) (:view ui)))
    ui))
