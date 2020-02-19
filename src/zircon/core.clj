(ns zircon.core
  (:require [zircon.app :refer [->app]]
            [zircon.view :as v]))

(defn build-ui [app-config views-config]
  (let [app (->app app-config)]
    (as-> {} m
          (assoc m :view (map #(v/make-view app %)
                              views-config))
          (assoc m :app app)
          (.dock (first (:view m))))))
