package org.example;

import org.example.dao.*;
import org.example.model.*;

import java.util.List;
import java.util.Scanner;

public class Menu {

    private final Scanner sc = new Scanner(System.in);

    private final AlunoDAO alunoDAO = new AlunoDAO();
    private final CasaDAO casaDAO = new CasaDAO();
    private final ProfessorDAO professorDAO = new ProfessorDAO();
    private final VarinhaDAO varinhaDAO = new VarinhaDAO();
    private final AnimalDAO animalDAO = new AnimalDAO();

    // =======================
    // MENU PRINCIPAL
    // =======================
    public void iniciar() {
        int opcao;

        do {
            System.out.println("\n===== SISTEMA HOGWARTS =====");
            System.out.println("1 - Gerenciar Alunos");
            System.out.println("2 - Gerenciar Casas");
            System.out.println("3 - Gerenciar Professores");
            System.out.println("4 - Gerenciar Varinhas");
            System.out.println("5 - Gerenciar Animais");
            System.out.println("6 - Consultas com JOIN");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");

            opcao = sc.nextInt();

            switch (opcao) {
                case 1 -> menuAluno();
                case 2 -> menuCasa();
                case 3 -> menuProfessor();
                case 4 -> menuVarinha();
                case 5 -> menuAnimal();
                case 6 -> menuConsultasJoin();
                case 0 -> System.out.println("Encerrando...");
                default -> System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }

    // =======================
    // SUBMENUS
    // =======================

    private void menuAluno() {
        int op;
        do {
            System.out.println("\n--- MENU ALUNO ---");
            System.out.println("1 - Inserir");
            System.out.println("2 - Atualizar");
            System.out.println("3 - Deletar");
            System.out.println("4 - Listar todos");
            System.out.println("5 - Buscar por ID");
            System.out.println("0 - Voltar");
            System.out.print("Escolha: ");
            op = sc.nextInt();

            switch (op) {
                case 1 -> inserirAluno();
                case 2 -> atualizarAluno();
                case 3 -> deletarAluno();
                case 4 -> listarAlunos();
                case 5 -> buscarAlunoPorId();
            }

        } while (op != 0);
    }

    private void menuCasa() {
        int op;
        do {
            System.out.println("\n--- MENU CASA ---");
            System.out.println("1 - Listar Casas");
            System.out.println("0 - Voltar");
            System.out.print("Escolha: ");
            op = sc.nextInt();

            if (op == 1) listarCasas();

        } while (op != 0);
    }

    private void menuProfessor() {
        int op;
        do {
            System.out.println("\n--- MENU PROFESSOR ---");
            System.out.println("1 - Listar Professores");
            System.out.println("0 - Voltar");
            System.out.print("Escolha: ");
            op = sc.nextInt();

            if (op == 1) listarProfessores();

        } while (op != 0);
    }

    private void menuVarinha() {
        int op;
        do {
            System.out.println("\n--- MENU VARINHA ---");
            System.out.println("1 - Listar Varinhas");
            System.out.println("0 - Voltar");
            System.out.print("Escolha: ");
            op = sc.nextInt();

            if (op == 1) listarVarinhas();

        } while (op != 0);
    }

    private void menuAnimal() {
        int op;
        do {
            System.out.println("\n--- MENU ANIMAL ---");
            System.out.println("1 - Listar Animais");
            System.out.println("0 - Voltar");
            System.out.print("Escolha: ");
            op = sc.nextInt();

            if (op == 1) listarAnimais();

        } while (op != 0);
    }

    // =======================
    // CONSULTAS COM JOIN
    // =======================
    private void menuConsultasJoin() {
        int op;
        do {
            System.out.println("\n--- CONSULTAS JOIN ---");
            System.out.println("1 - Alunos com suas Casas (JOIN simples)");
            System.out.println("2 - Alunos com Animais (JOIN)");
            System.out.println("3 - Professores e seus Alunos (JOIN N:N)");
            System.out.println("0 - Voltar");
            System.out.print("Escolha: ");
            op = sc.nextInt();

            switch (op) {
                case 1 -> joinAlunosCasas();
                case 2 -> joinAlunosAnimais();
                case 3 -> joinProfessoresAlunos();
            }

        } while (op != 0);
    }

    // =======================
    // IMPLEMENTAÇÃO CRUD
    // =======================

    private void inserirAluno() {
        sc.nextLine();

        Aluno a = new Aluno();

        System.out.print("Nome: ");
        a.nomeCompleto = sc.nextLine();

        System.out.print("Idade: ");
        a.idade = sc.nextInt();

        System.out.print("É puro sangue? (1=sim / 0=não): ");
        a.trouxaOuPuroSangue = sc.nextInt() == 1;

        System.out.print("Casa ID: ");
        a.casaId = sc.nextInt();

        sc.nextLine();
        System.out.print("Email: ");
        a.email = sc.nextLine();

        boolean ok = alunoDAO.insert(a);
        System.out.println(ok ? "Inserido com sucesso!" : "Erro ao inserir.");
    }

    private void atualizarAluno() {
        System.out.print("ID do aluno para atualizar: ");
        int id = sc.nextInt();

        Aluno a = alunoDAO.findById(id);
        if (a == null) {
            System.out.println("Aluno não encontrado.");
            return;
        }

        sc.nextLine();
        System.out.print("Novo nome: ");
        a.nomeCompleto = sc.nextLine();

        System.out.print("Nova idade: ");
        a.idade = sc.nextInt();

        System.out.print("Puro sangue? (1=sim / 0=não): ");
        a.trouxaOuPuroSangue = sc.nextInt() == 1;

        System.out.print("Casa ID: ");
        a.casaId = sc.nextInt();

        sc.nextLine();
        System.out.print("Novo email: ");
        a.email = sc.nextLine();

        boolean ok = alunoDAO.update(a);
        System.out.println(ok ? "Atualizado!" : "Erro ao atualizar.");
    }

    private void deletarAluno() {
        System.out.print("ID para deletar: ");
        int id = sc.nextInt();

        boolean ok = alunoDAO.delete(id);
        System.out.println(ok ? "Deletado!" : "Erro ao deletar.");
    }

    private void listarAlunos() {
        List<Aluno> lista = alunoDAO.findAll();
        lista.forEach(System.out::println);
    }

    private void buscarAlunoPorId() {
        System.out.print("ID: ");
        int id = sc.nextInt();

        Aluno a = alunoDAO.findById(id);
        System.out.println(a != null ? a : "Aluno não encontrado!");
    }

    private void listarCasas() { casaDAO.findAll().forEach(System.out::println); }
    private void listarProfessores() { professorDAO.findAll().forEach(System.out::println); }
    private void listarVarinhas() { varinhaDAO.findAll().forEach(System.out::println); }
    private void listarAnimais() { animalDAO.findAll().forEach(System.out::println); }

    // =======================
    // JOINs obrigatórios
    // =======================

    private void joinAlunosCasas() {
        System.out.println("\n=== JOIN: ALUNOS + CASAS ===");
        System.out.println("""
                SELECT a.id, a.nome_completo, c.nome AS casa
                FROM aluno a
                JOIN casa c ON c.casa_id = a.casa_casa_id;
                """);

        alunoDAO.joinAlunosComCasas()
                .forEach(System.out::println);
    }

    private void joinAlunosAnimais() {
        System.out.println("\n=== JOIN: ALUNOS + ANIMAIS ===");

        alunoDAO.joinAlunosComAnimais()
                .forEach(System.out::println);
    }

    private void joinProfessoresAlunos() {
        System.out.println("\n=== JOIN: PROFESSOR + ALUNOS (N:N) ===");

        professorDAO.joinProfessoresAlunos()
                .forEach(System.out::println);
    }
}
