;; NOTE Better remake .fdf generation with yield/lazy-seqs
;; TODO tests
;; TODO .fdf-parser
;; TODO labels validator

(ns dft.fdf
  (:require
   [clojure.string :as str]))


(defn- write-reduce-block [block-value]
  "Write raw string with block data."
  (reduce #(format "%s%n%s" %1 %2)
          (map #(str/join "  " %) block-value)))


(defn- write-fdf-option [option]
  "Write an option string as in `.fdf`-file."
  (if (:block option)
    (format "%s%n%s%n%s"
            (str "%block "      (:label option))
            (write-reduce-block (:value option))
            (str "%endblock "   (:label option)))
    (format "%-30s %-8s %s"
            (:label option)
            (:value option)
            (or (:units option) ""))))


(defn write-fdf [fdf-data]
  "Compile `.fdf` file content from hash-map data structure."
  (reduce #(format "%s%n%s" %1 %2)
          (map write-fdf-option fdf-data)))


;;; Example fdf data for H2O system:
;; (def h2o-fdf [{:label "SystemName"
;;                :value "Water molecule"}

;;               {:label "SystemLabel"
;;                :value "h2o"}

;;               {:label "NumberOfAtoms"
;;                :value 3}

;;               {:label "NumberOfSpecies"
;;                :value 2}

;;               {:label "ChemicalSpeciesLabel"
;;                :block true
;;                :value [[1 8 "O"]
;;                        [2 1 "H"]]}

;;               {:label "AtomicCoordinatesFormat"
;;                :value "Ang"}

;;               {:label "AtomicCoordinatesAndAtomicSpecies"
;;                :block true
;;                :value [[0.000   0.000  0.000  1]
;;                        [0.757   0.586  0.000  2]
;;                        [-0.757  0.586  0.000  2]]}

;;               {:label "MeshCutoff"
;;                :value 300.0
;;                :units "Ry"}

;;               {:label "DM.Require.Energy.Convergence"
;;                :value true}

;;               {:label "DM.Energy.Tolerance"
;;                :value 1.e-5
;;                :units "eV"}

;;               {:label "LatticeConstant"
;;                :value 12.00
;;                :units "Ang"}

;;               {:label "LatticeVectors"
;;                :block true
;;                :value [[1.0 0.0 0.0]
;;                        [0.0 1.0 0.0]
;;                        [0.0 0.0 1.0]]}])
