!/bin/bash
# This is a shell script to extract module names from a pom.xml file and run each module
# shell script to extract module names from a pom.xml file and run each module in a new vscode terminal instances in current window
# Clear the terminal
clear

if [[ "$TERM_PROGRAM" == "vscode" ]]; then
  echo $TERM_PROGRAM "is running the commands via apple terminal"
fi

# Extract module names and store them in an array
modules=($(grep "<module>" pom.xml | sed 's/<module>\(.*\)<\/module>/\1/'))

# Loop through the array and run each module
for module in "${modules[@]}"; do

    module_dir="$PWD/$module"

    # Check if the module directory exists and is a directory
    if [[ -d "$module_dir" ]]
    then
        echo "Running $module in $module_dir..."
    
        # Run the Spring Boot application in the module directory
        osascript -e "tell application \"Terminal\" to activate do script \"echo -n -e $module; cd $module_dir && ./mvnw clean install -DskipTests && ./mvnw spring-boot:run\""
    else
        echo "Directory $module_dir does not exist"
    fi
done