(ns zircon.tiles
  (:import (org.hexworks.zircon.api.color TileColor)
           (org.hexworks.zircon.api.data Tile)
           (org.hexworks.zircon.api Modifiers)
           (org.hexworks.zircon.api.modifier Modifier)))

;TODO some of the others have params, find a way to pass params at the same time
(def modifiers
  {:blink  (Modifiers/blink)
   :underline (Modifiers/underline)
   :crossed-out (Modifiers/crossedOut)
   :vertical-flip (Modifiers/verticalFlip)
   :horizontal-flip (Modifiers/horizontalFlip)
   :hidden (Modifiers/hidden)})

(defn parse-color [color]
  (if (vector? color)
    (let [[r g b a] color]
      (TileColor/create r g b a))
    (TileColor/fromString color)))

(defn create-tile [fg bg character]
  (-> (Tile/newBuilder)
      (.withForegroundColor (parse-color fg))
      (.withBackgroundColor (parse-color bg))
      (.withCharacter character)
      (.buildCharacterTile)))

(defn add-modifier [^Tile tile & mods]
  (.withAddedModifiers tile (into-array Modifier (replace modifiers mods))))
