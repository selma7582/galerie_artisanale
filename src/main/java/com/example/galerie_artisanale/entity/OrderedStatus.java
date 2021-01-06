package com.example.galerie_artisanale.entity;

public enum OrderedStatus {

    INVALID{
    },
    EN_ATTENTE{
        public String toString(){
            return "En attente";
        }

    },
    EN_COURS{
        public String toString(){
            return "En cours";
        }

    },
    ANNULER{
        public String toString(){
            return "Annulée";
        }
    },
    EXPEDIER{
        public String toString(){
            return "Expédiée";
        }

    },
    LIVRER{
        public String toString(){
            return "Livrée";
        }

    },
    CLOTURER{
        public String toString(){
            return "Cloturée";
        }

    },
    RETOURNER{
        public String toString(){
            return "Retournée";
        }

    },
    REMBOURSSE{
        public String toString(){
            return "Remboursée";
        }
    };


}
