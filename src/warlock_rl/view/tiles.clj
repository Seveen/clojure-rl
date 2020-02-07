(ns warlock-rl.view.tiles
  (:require [zircon.tiles :as t]))

(def tiles
  {:default (t/create-tile "#000000" "#000000" \ )
   :player (t/create-tile "#ffffff" "#000000" \@)
   :wall (t/create-tile "#ffffff" "#000000" \#)
   :floor (t/create-tile "#ffffff" "#000000" \.)
   :goblin (t/create-tile "#ff0000" "#000000" \g)})
