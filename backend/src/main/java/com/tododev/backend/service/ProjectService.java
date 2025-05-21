package com.tododev.backend.service;

import com.tododev.backend.model.*;
import com.tododev.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private OrganizacaoRepository organizacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProjectUserRepository projectUserRepository;

    public Project createProject(Long organizacaoId, Long managerId, String nome, String descricao) {
        Optional<Organizacao> organizacaoOpt = organizacaoRepository.findById(organizacaoId);
        Optional<Usuario> managerOpt = usuarioRepository.findById(managerId);

        if (organizacaoOpt.isEmpty() || managerOpt.isEmpty()) {
            throw new IllegalArgumentException("Organizacao or Manager not found");
        }

        Project project = new Project();
        project.setNome(nome);
        project.setDescricao(descricao);
        project.setOrganizacao(organizacaoOpt.get());
        project.setManager(managerOpt.get());
        project.setStatus(ProjectStatus.PENDING);
        project.setCreationDate(LocalDateTime.now());

        Project savedProject = projectRepository.save(project);

        // Assign manager as MANAGER role in project users
        ProjectUser projectUser = new ProjectUser();
        projectUser.setProject(savedProject);
        projectUser.setUsuario(managerOpt.get());
        projectUser.setRole(Role.MANAGER);
        projectUserRepository.save(projectUser);

        return savedProject;
    }

    public List<Project> getProjectsByOrganizacao(Long organizacaoId) {
        return projectRepository.findByOrganizacaoId(organizacaoId);
    }

    public Optional<Project> getProjectById(Long projectId) {
        return projectRepository.findById(projectId);
    }

    // Additional methods for updating, deleting projects can be added here
}
