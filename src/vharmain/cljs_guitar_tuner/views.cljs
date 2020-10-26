(ns vharmain.cljs-guitar-tuner.views
  (:require ["tone" :as tone]
            [reagent.core :as r]
            [vharmain.cljs-guitar-tuner.fsm :as player-state]))

(def synths
  {:synth (doto (tone/Synth.) (.toDestination))
   :fm    (doto (tone/FMSynth.) (.toDestination))
   :am    (doto (tone/AMSynth.) (.toDestination))
   :metal (doto (tone/MetalSynth.) (.toDestination))
   :duo   (doto (tone/DuoSynth.) (.toDestination))
   :pluck (doto (tone/PluckSynth.) (.toDestination))
   :poly  (doto (tone/PolySynth.) (.toDestination))})

(def sounds
  [{:note "E2"}
   {:note "A2"}
   {:note "D3"}
   {:note "G3"}
   {:note "B3"}
   {:note "E4"}])

(def state
  (r/atom {:synth (:synth synths)}))

(defn synth-selector []
  [:<>
   [:label {:for "synth-selector"} "Synth"]
   (into
    [:select
     {:id        "synth-selector"
      :on-change
      (fn [evt]
        (let [kw (-> evt .-target .-value keyword)]
          (swap! state assoc :synth (kw synths))))}]
    (for [[k _] synths]
      [:option {:value k} (name k)]))])

(defn notes []
  (into
   [:<>]
   (for [sound sounds]
     [:button
      {:on-click (fn [] (player-state/transition (:synth @state) sound))}
      (:note sound)])))

(defn app []
  [:div
   [:h1 "Guitar tuner"]
   [:div
    [synth-selector]]

   [notes]])
