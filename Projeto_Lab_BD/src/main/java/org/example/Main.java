package org.example;

// Classe principal do projeto.
// Ela só serve para iniciar o menu do sistema.
public class Main {

    public static void main(String[] args) {

        // Cria um objeto da classe Menu
        Menu menu = new Menu();

        // Chama o método que mostra o menu e fica lendo as opções
        menu.iniciar();
    }
}
