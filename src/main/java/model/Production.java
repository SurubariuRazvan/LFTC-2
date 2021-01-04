package model;

import java.util.Arrays;

class Production {
    private final String prodHead; // head
    private final String[] prodRhs; // right hand side

    public Production(String prodHead, String[] prodRhs) {
        this.prodHead = prodHead;
        this.prodRhs = prodRhs;
    }

    @Override
    public int hashCode() {
        int result = getProdHead() != null ? getProdHead().hashCode() : 0;
        result = 31 * result + Arrays.hashCode(getProdRhs());
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Production)) {
            return false;
        } else {
            Production p = (Production) o;
            return getProdHead().equals(p.getProdHead()) && Arrays.equals(getProdRhs(), p.getProdRhs());
        }
    }

    public String getProdHead() {
        return prodHead;
    }

    public String[] getProdRhs() {
        return prodRhs;
    }
}
