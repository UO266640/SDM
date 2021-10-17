package es.uniovi.eii.bestfood;

import android.os.Parcel;
import android.os.Parcelable;

public class Comida implements Parcelable {

    public static final Creator<Comida> CREATOR = new Creator<Comida>() {
        @Override
        public Comida createFromParcel(Parcel in) {
            return new Comida(in);
        }

        @Override
        public Comida[] newArray(int size) {
            return new Comida[size];
        }
    };
    String nombre;
    int puntuacion;

    protected Comida(Parcel in) {
        nombre = in.readString();
        puntuacion = in.readInt();
    }

    public Comida(String nombre, int puntuacion) {
        this.nombre = nombre;
        this.puntuacion = puntuacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeInt(puntuacion);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
