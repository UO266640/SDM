package es.uniovi.eii.bestfood.modelo;

import android.os.Parcel;
import android.os.Parcelable;

public class Comida implements Parcelable {


    String id;
    String nombre;

    String salt;
    String proteins;
    String carbohydrates;
    String energy;

    String saturated;
    String scoreLetter;

    String marca;
    String imagen;


    protected Comida(Parcel in) {
        id = in.readString();
        nombre = in.readString();
        salt = in.readString();
        proteins = in.readString();
        carbohydrates = in.readString();
        energy = in.readString();
        saturated = in.readString();
        scoreLetter = in.readString();
        marca = in.readString();
        imagen = in.readString();
    }

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

    public String getId() {
        return id;
    }

    public String getImagen() {
        return imagen;
    }

    public Comida(String id, String nombre, String salt, String proteins, String carbohydrates, String energy, String saturated, String scoreLetter, String marca, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.salt = salt;
        this.proteins = proteins;
        this.carbohydrates = carbohydrates;
        this.energy = energy;
        this.saturated = saturated;
        this.scoreLetter = scoreLetter;
        this.marca = marca;
        this.imagen = imagen;
    }

    public Comida(String nombre, String imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
    }


    public String getMarca() {
        return marca;
    }


    public String getSalt() {
        return salt;
    }

    public String getCarbohydrates() {
        return carbohydrates;
    }

    public String getEnergy() {
        return energy;
    }

    public String getProteins() {
        return proteins;
    }

    public String getSaturated() {
        return saturated;
    }

    public String getScoreLetter() {
        return scoreLetter;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nombre);
        dest.writeString(salt);
        dest.writeString(proteins);
        dest.writeString(carbohydrates);
        dest.writeString(energy);
        dest.writeString(saturated);
        dest.writeString(scoreLetter);
        dest.writeString(marca);
        dest.writeString(imagen);
    }
}
