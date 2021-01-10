package com.example.galerie_artisanale.entity;

public enum OrderedStatus {

    INVALID("Invalid") {
        @Override
        public OrderedStatus[] getNextStatus() {
            return new OrderedStatus[0];
        }
    }, // Only User can update it
    EN_ATTENTE("En attente") {
        @Override
        public OrderedStatus[] getNextStatus() {
            return new OrderedStatus[]{
                    OrderedStatus.EN_COURS,
                    OrderedStatus.ANNULER};
        }
    },
    EN_COURS("En cours") {
        @Override
        public OrderedStatus[] getNextStatus() {
            return new OrderedStatus[]{OrderedStatus.ANNULER,
                    OrderedStatus.EXPEDIER};
        }
    },
    ANNULER("Annulée") {
        @Override
        public OrderedStatus[] getNextStatus() {
            return new OrderedStatus[]{OrderedStatus.CLOTURER};
        }
    },
    EXPEDIER("Expédiée") {
        @Override
        public OrderedStatus[] getNextStatus() {
            return new OrderedStatus[]{OrderedStatus.LIVRER};
        }
    },
    LIVRER("Livrée") {
        @Override
        public OrderedStatus[] getNextStatus() {
            return new OrderedStatus[]{ OrderedStatus.CLOTURER /** apres 14 jours*/};
        } // arrivé chez le client
    },
    CLOTURER("Cloturée") {
        @Override
        public OrderedStatus[] getNextStatus() {
            return new OrderedStatus[0];
        }
    },
    RETOURNER("Retournée") {
        @Override
        public OrderedStatus[] getNextStatus() {
            return new OrderedStatus[]{OrderedStatus.REMBOURSSE};
        } // par le client
    },
    REMBOURSSE("Remboursée") {
        @Override
        public OrderedStatus[] getNextStatus() {
            return new OrderedStatus[]{ OrderedStatus.CLOTURER};
        }
    };

    private final String label ;

    OrderedStatus(String label ) {
        this.label = label;
    }

    public String toString(){
        return label;
    }

    public abstract OrderedStatus[] getNextStatus() ;
}
