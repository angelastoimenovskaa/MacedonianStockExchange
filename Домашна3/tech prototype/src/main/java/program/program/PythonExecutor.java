package program.program;

import jep.SharedInterpreter;
import jep.JepException;

import java.nio.file.Files;
import java.nio.file.Paths;

public class PythonExecutor {

    // Execute multiple Python script files
    public String executeMultiplePythonScripts(String[] scriptPaths) {
        StringBuilder results = new StringBuilder();

        try (SharedInterpreter jep = new SharedInterpreter()) {
            for (String scriptPath : scriptPaths) {
                if (!Files.exists(Paths.get(scriptPath))) {
                    results.append("Script not found: ").append(scriptPath).append("\n");
                    continue;
                }

                try {
                    jep.runScript(scriptPath); // Execute the script file
                    // Ensure each script assigns a value to a 'result' variable
                    Object result = jep.getValue("result");
                    results.append("Result of script: ").append(scriptPath).append("\n");
                    results.append(result != null ? result.toString() : "No result").append("\n");
                } catch (JepException e) {
                    results.append("Error executing script: ").append(scriptPath).append("\n");
                    results.append(e.getMessage()).append("\n");
                }
            }
        } catch (JepException e) {
            results.append("Error initializing SharedInterpreter: ").append(e.getMessage()).append("\n");
        }

        return results.toString();
    }

    // Optional helper method to read script contents (if needed)
    private String readScript(String path) throws Exception {
        return new String(Files.readAllBytes(Paths.get(path)));
    }
}
