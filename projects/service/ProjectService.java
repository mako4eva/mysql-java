package projects.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import projects.dao.ProjectDao;
import projects.entity.Project;
import projects.exception.DbException;

public class ProjectService {
	private ProjectDao projectDao = new ProjectDao();

	public Project addProject(Project project) {
		return projectDao.insertProject(project);
	}

	public List<Project> fetchAllProjects() {
		return projectDao.fetchAllProjects();
	}

	public Project fetchProjectById(Integer projectId) {
		return projectDao.fetchProjectById(projectId).orElseThrow(() -> new NoSuchElementException("Project with project ID=" + projectId + " does not exist."));
	}

	public void modifyProjectDetails(Project curProject) {
		if (!projectDao.modifyProjectDetails(curProject)) {
			throw new DbException("Project with ID=" + curProject.getProjectId() + " does not exist.");
		}
		
	}

	public void deleteProject(Integer input) {
		if (!projectDao.deleteProject(input)) {
			throw new DbException("Project with ID=" + input + " does not exist.");
		}
		
	}
}
