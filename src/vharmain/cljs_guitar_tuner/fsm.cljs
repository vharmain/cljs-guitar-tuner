(ns vharmain.cljs-guitar-tuner.fsm
  (:require [statecharts.core :as fsm]))

(defn play! [^js synth note]
  (.triggerAttack synth note))

(defn stop! [^js synth]
  (when synth
    (.triggerRelease synth)))

(def machine
  (fsm/machine
   {:id      :guitar-tuner
    :initial :not-playing
    :context nil
    :states
    {:playing
     {:entry
      (fsm/assign
       (fn [state {:keys [synth sound]}]
         (play! synth (:note sound))
         (assoc state :synth synth :sound sound)))
      :on
      {:play
       [{:guard
         ;; When the note that is clicked is already playing it works
         ;; as a toggle.
         (fn [state event]
           (= (:sound state) (:sound event)))
         :target :not-playing}
        {:target :playing}]}}

     :not-playing
     {:entry
      (fn [state _]
        (stop! (:synth state)))
      :on
      {:play
       {:target :playing}}}}}))

(def service (fsm/service machine))

(defn transition [^js synth sound]
  (fsm/send service {:type :play :sound sound :synth synth}))

(defn start []
  (fsm/start service))
