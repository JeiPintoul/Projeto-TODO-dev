package com.tododev.backend.service;

import com.tododev.backend.model.*;
import com.tododev.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskArtifactRepository taskArtifactRepository;

    public Task createTask(Long projectId, Long managerId, String titulo, String descricao) {
        Optional<Project> projectOpt = projectRepository.findById(projectId);
        Optional<Usuario> managerOpt = usuarioRepository.findById(managerId);

        if (projectOpt.isEmpty() || managerOpt.isEmpty()) {
            throw new IllegalArgumentException("Project or Manager not found");
        }

        Task task = new Task();
        task.setProject(projectOpt.get());
        task.setManager(managerOpt.get());
        task.setTitulo(titulo);
        task.setDescricao(descricao);
        task.setStatus(TaskStatus.PENDING);
        task.setCreationDate(LocalDateTime.now());

        return taskRepository.save(task);
    }

    public List<Task> getTasksByProject(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    public Optional<Task> getTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    public Task startTask(Long taskId, Long usuarioId) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);

        if (taskOpt.isEmpty() || usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Task or Usuario not found");
        }

        Task task = taskOpt.get();
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setUsuarioEmAndamento(usuarioOpt.get());
        // Optionally reset endDate and timeSpent when starting
        task.setEndDate(null);
        task.setTimeSpent(null);

        return taskRepository.save(task);
    }

    public Task completeTask(Long taskId, Long usuarioId) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);

        if (taskOpt.isEmpty() || usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Task or Usuario not found");
        }

        Task task = taskOpt.get();
        task.setStatus(TaskStatus.DONE);
        task.setUsuarioConcluido(usuarioOpt.get());
        LocalDateTime now = LocalDateTime.now();
        task.setEndDate(now);

        if (task.getCreationDate() != null) {
            Duration duration = Duration.between(task.getCreationDate(), now);
            task.setTimeSpent(duration.toHours());
        } else {
            task.setTimeSpent(null);
        }

        return taskRepository.save(task);
    }

    // Additional methods for updating, deleting tasks, adding artifacts, etc.
}
