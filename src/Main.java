import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        tasks.add(new Task("stirat", 3));
        tasks.add(new Task("kayfovat", 2));

        while (true) {
            System.out.println("\nДобро пожаловать в приложение \"Управление списком задач\"!");
            System.out.println("1. Добавить задачу");
            System.out.println("2. Удалить задачу");
            System.out.println("3. Отредактировать задачу");
            System.out.println("4. Изменить статус задачи");
            System.out.println("5. Показать все задачи");
            System.out.println("6. Фильтровать задачи по статусу");
            System.out.println("7. Найти задачу по ключевому слову");
            System.out.println("8. Показать статистику");
            System.out.println("9. Выход");
            System.out.print("\nВыберите действие: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> addTask();
                case 2 -> removeTask();
                case 3 -> editTask();
                case 4 -> changeStatus();
                case 5 -> showTasks();
                case 6 -> filterTasks();
                case 7 -> searchTasks();
                case 8 -> showStats();
                case 9 -> {
                    System.out.println("Программа завершена.");
                    return;
                }
                default -> System.out.println("Неверный выбор. Повторите.");
            }
        }
    }

    private static void addTask() {
        System.out.print("Введите название задачи: ");
        String name = scanner.nextLine();
        System.out.print("Введите приоритет (целое число): ");
        int priority = Integer.parseInt(scanner.nextLine());
        tasks.add(new Task(name, priority));
        System.out.println("Задача добавлена!");
    }

    private static void removeTask() {
        int index = getTaskIndex();
        if (index != -1) {
            tasks.remove(index);
            System.out.println("Задача удалена.");
        }
    }

    private static void editTask() {
        int index = getTaskIndex();
        if (index != -1) {
            System.out.print("Введите новое название задачи: ");
            tasks.get(index).setName(scanner.nextLine());
            System.out.print("Введите новый приоритет: ");
            tasks.get(index).setPriority(Integer.parseInt(scanner.nextLine()));
            System.out.println("Задача обновлена.");
        }
    }

    private static void showTasks() {
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст.");
            return;
        }
        System.out.println("1. Сортировать по приоритету\n2. Сортировать по дате создания");
        int sortChoice = Integer.parseInt(scanner.nextLine());
        List<Task> sorted = new ArrayList<>(tasks);
        if (sortChoice == 1) {
            sorted.sort(Comparator.comparing(Task::getPriority).reversed());
        } else {
            sorted.sort(Comparator.comparing(Task::getCreationDate));
        }
        for (int i = 0; i < sorted.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, sorted.get(i));
        }
    }

    private static void filterTasks() {
        System.out.println("Список задач отсортированных:");
        Task.Status targetStatus = Task.Status.В_ПРОЦЕССЕ;
        List<Task> filtered = tasks.stream()
                .filter(task -> task.getStatus() == targetStatus)
                .filter(task -> task.getPriority() >= 1 && task.getPriority() <= 5)
                .toList();

        for (int i = 0; i < filtered.size(); i++) {
            System.out.println((i + 1) + ". " + filtered.get(i));
        }
    }

    private static void searchTasks() {
        System.out.print("Введите ключевое слово: ");
        String keyword = scanner.nextLine().toLowerCase();
        tasks.stream().filter(t -> t.getName().toLowerCase().contains(keyword))
                .forEach(t -> System.out.printf("%s\n", t));
    }

    private static void changeStatus() {
        int index = getTaskIndex();
        if (index != -1) {
            System.out.println("Выберите новый статус:\n1. ВЫПОЛНЕНА\n2. В_ПРОЦЕССЕ\n3. НЕВЫПОЛНЕНА");
            int statusChoice = Integer.parseInt(scanner.nextLine());
            Task.Status status = Task.Status.values()[statusChoice - 1];
            tasks.get(index).setStatus(status);
            System.out.println("Статус задачи обновлён. Время выполнения: " +
                    ((status == Task.Status.ВЫПОЛНЕНА) ? tasks.get(index).getCompletedAt() : "Отсутствует"));
        }
    }

    private static void showStats() {
        Map<Task.Status, Long> countByStatus = tasks.stream()
                .collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));
        System.out.println("Статистика задач:");
        for (Task.Status status : Task.Status.values()) {
            System.out.printf("- %s: %d\n", status, countByStatus.getOrDefault(status, 0L));
        }
        double avgPriority = tasks.stream().mapToInt(Task::getPriority).average().orElse(0);
        System.out.printf("Средний приоритет: %.2f\n", avgPriority);
    }

    private static int getTaskIndex() {
        System.out.print("Введите номер задачи: ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;
        if (index < 0 || index >= tasks.size()) {
            System.out.println("Неверный номер задачи.");
            return -1;
        }
        return index;
    }
}