(ns warlock-rl.ui
  (:require [com.rpl.specter :refer :all]
            [warlock-rl.system :refer [update-world]]))

(def ui-state (atom {:commands []
                     :stack    []
                     :viewport {:position [0 0]
                                :size     [60 40]}}))

(defn peek-ui-state [state]
  (peek (select-first [:stack] state)))

(defn pop-ui-state [state]
  (transform [:stack] #(pop %) state))

(defn push-ui-state [state new-state]
  (transform [:stack] #(conj % new-state) state))

(defn switch-view [state]
  (println "Coin")
  state)

(def ui-command
  #{:targeting})

(defn update-ui [command]
  (if (contains? ui-command command)
    (swap! ui-state
           (fn [state command]
             (case command
               state))
           command)
    (update-world command)))