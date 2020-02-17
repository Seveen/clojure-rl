(ns zircon.game-area
  (:require [warlock-rl.view.tiles :as t]
            [warlock-rl.system :refer [MAP DRAWABLE ENTITIES VIEWPORT]]
            [com.rpl.specter :refer [select select-first ALL]]
            [zircon.interop :as i])
  (:import (org.hexworks.zircon.api.data Size3D Block Position3D)
           (org.hexworks.zircon.api GameComponents)))

;todo: all of that is more of a "lens" that is defined in the game-area
;definition in user space
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

(defn area [size]
  (.build
    (doto (GameComponents/newGameAreaBuilder)
      (.withActualSize (i/vec->size3D (conj size 1)))
      (.withVisibleSize (i/vec->size3D (conj size 1))))))

;TODO: use watch on an atom???
(defn paint-world [state]
  (doseq [[[x y] glyph] (see-world state)
          :let [tile (t/tiles glyph)]]
    (.setBlockAt area
                 (Position3D/create x y 0)
                 (-> (Block/newBuilder)
                     (.withEmptyTile tile)
                     (.build)))))

;une seule fonction:
;on lui file une size, une lens et l'atom à regarder
;elle instancie le game area, et ajoute un watcher sur l'atom en utilisant la lens
;elle renvoie la game area
