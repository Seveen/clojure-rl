(ns warlock-rl.system
  (:require [com.rpl.specter :refer [path pred setval select select-first
                                     keypath comp-paths ALL]]
            [clj-uuid :as uuid]))

(defn create-map []
  (reduce merge
          (for [i (range 120)
                j (range 80)]
            (if (or (= i 0) (= i 119) (= j 0) (= j 79))
              {[i j] {:glyph :wall :walkable false}}
              {[i j] {:glyph :floor :walkable true}}))))

(def ENTITIES (path [:entities ALL]))
(def DRAWABLE (path [ENTITIES (pred :glyph)]))
(def PLAYER (path [ENTITIES (pred :player?)]))

(def MAP (path [:map]))
(def VIEWPORT (path [:viewport]))

(defn get-player [state]
  (select-first [PLAYER] state))

(defn get-walkable [state pos]
  (select-first [MAP (keypath pos :walkable)] state))

(defn get-entity-at-pos [state pos]
  (select-first [ENTITIES (pred #(= (:position %) pos))] state))

(def state (atom {:entities [{:id       (uuid/v1)
                              :player?  true
                              :glyph    :player
                              :faction  :player
                              :position [20 10]}
                             {:id       (uuid/v1)
                              :glyph    :goblin
                              :faction  :baddies
                              :position [10 10]}]
                  :map      (create-map)
                  :viewport {:position [0 0]
                             :size     [60 40]}}))

(defn change-position [position direction]
  (let [[x y] position]
    (case direction
      :right [(inc x) y]
      :left [(dec x) y]
      :up [x (dec y)]
      :down [x (inc y)]
      position)))

(defn center-viewport [[x y] state]
  (let [[width height] (select-first [VIEWPORT :size] state)]
    (setval [VIEWPORT :position]
            [(- x (/ width 2))
             (- y (/ height 2))]
            state)))

(defn move [state path direction]
  (let [pos-path (comp-paths path :position)
        new-pos (change-position (first (select [path :position] state)) direction)]
    (if (get-walkable state new-pos)
      (center-viewport new-pos (setval pos-path new-pos state))
      state)))

(defn process-command [state command]
  (case command
    :right (move state PLAYER :right)
    :left (move state PLAYER :left)
    :up (move state PLAYER :up)
    :down (move state PLAYER :down)
    state))

(defn update-world [command]
  (swap! state process-command command))
