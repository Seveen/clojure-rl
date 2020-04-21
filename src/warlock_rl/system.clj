(ns warlock-rl.system
  (:require [com.rpl.specter :refer [path pred setval select select-first
                                     transform keypath comp-paths ALL NONE]]
            [clj-uuid :as uuid])
  (:import (clojure.lang PersistentQueue)))

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

(defn update-entity-field [state entity field transformation]
  (transform [ENTITIES (pred #(= (:id %) (:id entity))) field]
             transformation state))

(defn set-entity-field [state entity field new-value]
  (setval [ENTITIES (pred #(= (:id %) (:id entity))) field] new-value state))

(defn remove-entity [state entity]
  (setval [ENTITIES (pred #(= (:id %) (:id entity)))] NONE state))

(defn push-to-log [state message]
  (transform [:log] #(conj % message) state))

(def state (atom {:entities [{:id       (uuid/v1)
                              :player?  true
                              :glyph    :player
                              :name     "Player"
                              :faction  :player
                              :position [20 10]
                              :attack   10
                              :defense  1
                              :health   3}
                             {:id       (uuid/v1)
                              :glyph    :goblin
                              :name     "Goblin"
                              :faction  :baddies
                              :position [10 10]
                              :attack   10
                              :defense  1
                              :health   30}]
                  :map      (create-map)
                  :viewport {:position [0 0]
                             :size     [60 40]}
                  :log      []}))

(defn change-position [position direction]
  (let [[x y] position]
    (case direction
      :right [(inc x) y]
      :left [(dec x) y]
      :up [x (dec y)]
      :down [x (inc y)]
      position)))

(defn center-viewport [state [x y]]
  (let [[width height] (select-first [VIEWPORT :size] state)]
    (setval [VIEWPORT :position]
            [(- x (/ width 2))
             (- y (/ height 2))]
            state)))

(defn compute-damage [attack defense]
  (let [damage (- attack defense)]
    (if (neg? damage)
      0
      damage)))

(defn attack [state attacker defender]
  (let [damage (compute-damage (:attack attacker) (:defense defender))
        dies? (neg? (- (:health defender) damage))]
    (if dies?
      (-> state
          (remove-entity defender)
          (push-to-log (str "The " (:name defender) " died!")))
      (-> state
          (update-entity-field defender :health #(- % damage))
          (push-to-log (str "The " (:name attacker) " hits the " (:name defender) " for " damage " damage."))))))

(defn bump-into [state interactor interacted]
  (if (= (:faction interactor) (:faction interacted))
    state
    (attack state interactor interacted)))

(defn move [state path direction]
  (let [entity (select-first path state)
        new-pos (change-position (:position entity) direction)]
    (if-let [other (get-entity-at-pos state new-pos)]
      (bump-into state entity other)
      (if (get-walkable state new-pos)
        (-> state
            (set-entity-field entity :position new-pos)
            (center-viewport new-pos))
        state))))

(defn process-command [state command]
  (->> state
      (setval [:log] [])
      (#(case command
          :no-op %
          :right (move % PLAYER :right)
          :left (move % PLAYER :left)
          :up (move % PLAYER :up)
          :down (move % PLAYER :down)
          %))))

(defn update-world [command]
  (swap! state process-command command))
