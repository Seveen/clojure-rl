(ns zircon.game-area
  (:require [warlock-rl.view.tiles :as t]
            [zircon.interop :as i])
  (:import (org.hexworks.zircon.api.data Block Position3D)
           (org.hexworks.zircon.api GameComponents)))

(defn area [size lens state]
  (let [game-area (.build
                    (doto (GameComponents/newGameAreaBuilder)
                      (.withActualSize (i/vec->size3D (conj size 1)))
                      (.withVisibleSize (i/vec->size3D (conj size 1)))))
        paint-world (fn [lens state]
                      (doseq [[[x y] tile] (lens state)]
                        (.setBlockAt game-area
                                     (Position3D/create x y 0)
                                     (-> (Block/newBuilder)
                                         (.withEmptyTile tile)
                                         (.build)))))]
    (add-watch state :world-painter
               (fn [key atom old-state new-state]
                 (paint-world lens new-state)))
    game-area))
