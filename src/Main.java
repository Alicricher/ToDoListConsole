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
            int choice = readInt();

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

    private static int readInt() {
        while (true) {
            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            } else {
                System.out.print("Ошибка ввода. Введите целое число: ");
                scanner.next();
            }
        }
    }

    private static void addTask() {
        scanner.nextLine();
        System.out.print("Введите название задачи: ");
        String name = scanner.nextLine();
        System.out.print("Введите приоритет (целое число): ");
        int priority = readInt();
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
            scanner.nextLine();
            System.out.print("Введите новое название задачи: ");
            tasks.get(index).setName(scanner.nextLine());
            System.out.print("Введите новый приоритет: ");
            tasks.get(index).setPriority(readInt());
            System.out.println("Задача обновлена.");
        }
    }

    private static void showTasks() {
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст.");
            return;
        }
        System.out.println("1. Сортировать по приоритету\n2. Сортировать по дате создания");
        int sortChoice = readInt();
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
        System.out.println("Фильтрация задач:");
        System.out.println("1. По статусу\n2. По диапазону приоритетов");
        int filterChoice = readInt();

        if (filterChoice == 1) {
            System.out.println("Выберите статус (1. НЕВЫПОЛНЕНА, 2. В_ПРОЦЕССЕ, 3. ВЫПОЛНЕНА): ");
            int statusChoice = readInt();
            TaskStatus selectedStatus = TaskStatus.values()[statusChoice - 1];
            tasks.stream()
                    .filter(task -> task.getStatus() == selectedStatus)
                    .forEach(task -> System.out.println(task));
        } else if (filterChoice == 2) {
            System.out.print("Введите минимальный приоритет: ");
            int min = readInt();
            System.out.print("Введите максимальный приоритет: ");
            int max = readInt();
            tasks.stream()
                    .filter(task -> task.getPriority() >= min && task.getPriority() <= max)
                    .forEach(task -> System.out.println(task));
        } else {
            System.out.println("Неверный выбор фильтрации.");
        }
    }

    private static void searchTasks() {
        scanner.nextLine();
        System.out.print("Введите ключевое слово: ");
        String keyword = scanner.nextLine().toLowerCase();
        tasks.stream().filter(t -> t.getName().toLowerCase().contains(keyword))
                .forEach(t -> System.out.printf("%s\n", t));
    }

    private static void changeStatus() {
        int index = getTaskIndex();
        if (index != -1) {
            System.out.println("Выберите новый статус:\n1. ВЫПОЛНЕНА\n2. В_ПРОЦЕССЕ\n3. НЕВЫПОЛНЕНА");
            int statusChoice = readInt();
            TaskStatus status = TaskStatus.values()[statusChoice - 1];
            tasks.get(index).setStatus(status);
            System.out.println("Статус задачи обновлён. Время выполнения: " +
                    ((status == TaskStatus.ВЫПОЛНЕНА) ? tasks.get(index).getCompletedAt() : "Отсутствует"));
        }
    }

    private static void showStats() {
        Map<TaskStatus, Long> countByStatus = tasks.stream()
                .collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));
        System.out.println("Статистика задач:");
        for (TaskStatus status : TaskStatus.values()) {
            System.out.printf("- %s: %d\n", status, countByStatus.getOrDefault(status, 0L));
        }
        double avgPriority = tasks.stream().mapToInt(Task::getPriority).average().orElse(0);
        System.out.printf("Средний приоритет: %.2f\n", avgPriority);
    }

    private static int getTaskIndex() {
        System.out.print("Введите номер задачи: ");
        int index = readInt() - 1;
        if (index < 0 || index >= tasks.size()) {
            System.out.println("Неверный номер задачи.");
            return -1;
        }
        return index;
    }
}