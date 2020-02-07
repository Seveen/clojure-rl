(ns zircon.tiles
  (:import (org.hexworks.zircon.api.color TileColor)
           (org.hexworks.zircon.api.data Tile)))

(defn create-tile [fg bg character]
  (-> (Tile/newBuilder)
      (.withForegroundColor (TileColor/fromString fg))
      (.withBackgroundColor (TileColor/fromString bg))
      (.withCharacter character)
      (.buildCharacterTile)))