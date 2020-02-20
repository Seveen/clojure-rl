(ns warlock-rl.view.lens
  (:require [warlock-rl.system :refer [MAP DRAWABLE ENTITIES VIEWPORT]]
            [warlock-rl.view.tiles :as t]
            [com.rpl.specter :refer [select select-first ALL]]))

(defn- empty-tilemap [x y]
  (apply merge (for [i (range x)]
                 (into {} (for [j (range y)]
                            [[i j] (t/tiles :default)])))))

(defn- see-map [x-min x-max y-min y-max state]
  (into {} (for [[[x y] map-tile] (select [MAP ALL] state)
                 :when (and (< x x-max)
                            (>= x x-min)
                            (< y y-max)
                            (>= y y-min))
                 :let [tile (t/tiles (:glyph map-tile))]]
             [[(- x x-min) (- y y-min)] tile])))

(defn- see-entities [x-min x-max y-min y-max state]
  (into {} (for [entity (select [ENTITIES] state)
                 :let [tile (t/tiles (:glyph entity))
                       [x y] (:position entity)]
                 :when (and (< x x-max)
                            (>= x x-min)
                            (< y y-max)
                            (>= y y-min))]
             [[(- x x-min) (- y y-min)] tile])))

(defn see-world [state]
  (let [[x-off y-off] (select-first [VIEWPORT :position] state)
        [width height] (select-first [VIEWPORT :size] state)
        x-max (+ x-off width)
        y-max (+ y-off height)]
    (apply merge (empty-tilemap width height)
           (see-map x-off x-max y-off y-max state)
           (see-entities x-off x-max y-off y-max state))))
