package com.tododev.backend.controller;

import com.tododev.backend.model.Project;
import com.tododev.backend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestParam Long organizacaoId,
                                                 @RequestParam Long managerId,
                                                 @RequestParam String nome,
                                                 @RequestParam(required = false) String descricao) {
        Project project = projectService.createProject(organizacaoId, managerId, nome, descricao);
        return ResponseEntity.ok(project);
    }

    @GetMapping("/organization/{organizacaoId}")
    public ResponseEntity<List<Project>> getProjectsByOrganizacao(@PathVariable Long organizacaoId) {
        List<Project> projects = projectService.getProjectsByOrganizacao(organizacaoId);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long projectId) {
        return projectService.getProjectById(projectId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
