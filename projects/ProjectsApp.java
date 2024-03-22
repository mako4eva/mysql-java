package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

public class ProjectsApp {
	private Scanner scanner = new Scanner(System.in);
	private ProjectService projectService = new ProjectService();
	private Project curProject = null;
	// @formatter:off

	private List<String> operations = List.of(
		"1) Add a project",
		"2) List projects",
		"3) Select a project",
		"4) Update project details",
		"5) Delete a project"
		);

	// @formatter:on

	public static void main(String[] args) {
		new ProjectsApp().processUserSelections();

	}

	private void processUserSelections() {
		boolean done = false;
		while (!done) {
			try {
				int selection = getUserSelection();
				switch (selection) {
				case -1:
					done = exitMenu();
					break;
				case 1:
					createProject();
					break;
				case 2:
					listProjects();
					break;
				case 3:
					selectProject();
					break;
				case 4:
					updateProjectDetails();
					break;
				case 5:
					deleteProject();
					break;
				default:
					System.out.println("\n" + selection + "is not a valid selection. Try again.");
					break;
				}
			} catch (Exception e) {
				System.out.println("\nError: " + e + " Try again.");
			}
		}
	}

	private void deleteProject() {
		listProjects();
		Integer input = getIntInput("Enter a project ID to delete a project");
		projectService.deleteProject(input);
		System.out.println("Project was deleted!");
		if (curProject == null || curProject.getProjectId() == input) curProject = null;
	}

	private void updateProjectDetails() {
		if (curProject == null) {
			System.out.println("Please select a project.");
		} else {
			String projectName = getStringInput("Enter the project name ["+curProject.getProjectName()+"]");
			BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours [" + curProject.getEstimatedHours() + "]");
			BigDecimal actualHours = getDecimalInput("Enter the actual hours [" + curProject.getActualHours() + "]");
			Integer difficulty = getIntInput("Enter the project difficulty (1-5) [" + curProject.getDifficulty() + "]");
			String notes = getStringInput("Enter the project notes [" + curProject.getNotes() + "]");
			Project project = new Project();
			project.setProjectName(projectName == null ? curProject.getProjectName() : projectName);
			project.setEstimatedHours(estimatedHours == null ? curProject.getEstimatedHours() : estimatedHours);
			project.setActualHours(actualHours == null ? curProject.getActualHours() : actualHours);
			project.setDifficulty(difficulty == null ? curProject.getDifficulty() : difficulty);
			project.setNotes(notes == null ? curProject.getNotes() : notes);
			project.setProjectId(curProject.getProjectId());
			projectService.modifyProjectDetails(project);
			curProject = projectService.fetchProjectById(project.getProjectId());
		}
	}

	private void selectProject() {
		listProjects();
		curProject = null;
		Integer input = getIntInput("Enter a project ID to select a project");
		curProject = projectService.fetchProjectById(input);
		if (curProject.equals(null)) System.out.println("Invalid project ID selected.");
		
	}

	private void listProjects() {
		List<Project> projects = projectService.fetchAllProjects();
		System.out.println("\nProjects:");
		for(Project project : projects) {
			System.out.println("   " + project.getProjectId() + ": " + project.getProjectName());
		}
		
	}

	private void createProject() {
		String projectName = getStringInput("Enter the project name");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours");
		Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
		String notes = getStringInput("Enter the project notes");
		Project project = new Project();
		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);
		Project dbProject = projectService.addProject(project);
		System.out.println("You have successfully created project: " + dbProject);
	}

	private BigDecimal getDecimalInput(String prompt) {
		String input = getStringInput(prompt);
		if (Objects.isNull(input)) {
			return null;
		}
		try {
			return new BigDecimal(input).setScale(2);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid decimal number.");
		}
	}

	private boolean exitMenu() {
		System.out.println("Exiting the menu.");
		return true;
	}

	private int getUserSelection() {
		printOperations();
		Integer input = getIntInput("Enter a menu selection");
		return Objects.isNull(input) ? -1 : input;
	}

	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);
		if (Objects.isNull(input)) {
			return null;
		}
		try {
			return Integer.valueOf(input);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid number.");
		}
	}

	private String getStringInput(String prompt) {
		System.out.print(prompt + ": ");
		String input = scanner.nextLine();
		return input.isBlank() ? null : input.trim();
	}

	private void printOperations() {
		System.out.println("\nThese are the available selections. Press the Enter key to quit:");
		operations.forEach(line -> System.out.println(" " + line));
		if(Objects.isNull(curProject)) {
			System.out.println("\nYou are not working with a project.");
		} else {
			System.out.println("\nYou are working with project: " + curProject);
		}
	
	}
}
