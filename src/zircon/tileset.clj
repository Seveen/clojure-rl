(ns zircon.tileset
  (:import (org.hexworks.zircon.api CP437TilesetResources)))

;TODO add tilesets from external resources
(defn cp437-tilesets
  [tileset]
  (eval `(. CP437TilesetResources ~(symbol tileset))))
