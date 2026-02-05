package sistema;

// Importa la clase Recuerdo del modelo
import model.Recuerdo;

// Interfaz que define el comportamiento de los módulos del sistema
public interface Modulo {

    // Metodo que se ejecuta cuando el recuerdo actual cambia
    // Cada módulo que implemente esta interfaz debe definir
    // cómo reaccionar o actualizarse con el recuerdo recibido
    void actualizar(Recuerdo recuerdoActual);
}
