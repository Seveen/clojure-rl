(ns zircon.game-area
  (:require [warlock-rl.view.tiles :as t]
            [warlock-rl.system :refer [MAP DRAWABLE ENTITIES VIEWPORT]]
            [com.rpl.specter :refer [select select-first ALL]])
  (:import (org.hexworks.zircon.api.data Size3D Block Position3D)
           (org.hexworks.zircon.api GameComponents)
           (org.hexworks.zircon.api.game.base BaseGameArea)))

(def empty-tilemap
  (apply merge (for [i (range 60)]
                 (into {} (for [j (range 40)]
                            [[i j] :default])))))

(defn- see-map [xmin xmax ymin ymax state]
  (into {} (for [[[x y] map-tile] (select [MAP ALL] state)
                 :when (and (< x xmax)
                            (>= x xmin)
                            (< y ymax)
                            (>= y ymin))
                 :let [glyph (:glyph map-tile)]]
             [[(- x xmin) (- y ymin)] glyph])))

(defn- see-entities [xmin xmax ymin ymax state]
  (into {} (for [entity (select [ENTITIES] state)
                 :let [glyph (:glyph entity)
                       [x y] (:position entity)]
                 :when (and (< x xmax)
                            (>= x xmin)
                            (< y ymax)
                            (>= y ymin))]
             [[(- x xmin) (- y ymin)] glyph])))

(defn- see-world [state]
  (let [[xoff yoff] (select-first [VIEWPORT :position] state)
        [width height] (select-first [VIEWPORT :size] state)
        xmax (+ xoff width)
        ymax (+ yoff height)]
    (apply merge empty-tilemap
           (see-map xoff xmax yoff ymax state)
           (see-entities xoff xmax yoff ymax state))))

(def area
  (.build
    (doto (GameComponents/newGameAreaBuilder)
      (.withActualSize (Size3D/create 60 40 1))
      (.withVisibleSize (Size3D/create 60 40 1)))))

(defn paint-world [state]
  (doseq [[[x y] glyph] (see-world state)
          :let [tile (t/tiles glyph)]]
    (.setBlockAt area
                 (Position3D/create x y 0)
                 (-> (Block/newBuilder)
                     (.withEmptyTile tile)
                     (.build)))))
(comment
  (defn make-area [config]
    (proxy [BaseGameArea] [])))