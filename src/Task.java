import java.time.LocalDate;
import java.time.LocalDateTime;

class Task {
    private String name;
    private int priority;
    private LocalDate creationDate;
    private LocalDateTime completedAt;
    private Status status;

    public enum Status {
        НЕВЫПОЛНЕНА, В_ПРОЦЕССЕ, ВЫПОЛНЕНА
    }

    public Task(String name, int priority) {
        this.name = name;
        this.priority = priority;
        this.creationDate = LocalDate.now();
        this.status = Status.НЕВЫПОЛНЕНА;
        this.completedAt = null;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public LocalDate getCreationDate() { return creationDate; }
    public Status getStatus() { return status; }
    public LocalDateTime getCompletedAt() { return completedAt; }

    public void setStatus(Status status) {
        this.status = status;
        if (status == Status.ВЫПОЛНЕНА) {
            this.completedAt = LocalDateTime.now();
        } else {
            this.completedAt = null;
        }
    }

    @Override
    public String toString() {
        String completionTime = (completedAt == null) ? "Отсутствует" : completedAt.toString();
        return name + " | Приоритет: " + priority +
                " | Статус: " + status +
                " | Дата создания: " + creationDate +
                " | Время выполнения: " + completionTime;
    }
}
