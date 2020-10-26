(ns vharmain.cljs-guitar-tuner.core
  (:require [reagent.dom :as rd]
            [vharmain.cljs-guitar-tuner.views :as views]
            [vharmain.cljs-guitar-tuner.fsm :as fsm]))

(defn ^:dev/after-load start
  []
  (fsm/start)
  (rd/render
   [views/app]
   (.getElementById js/document "app")))

(defn ^:export main
  []
  (start))
