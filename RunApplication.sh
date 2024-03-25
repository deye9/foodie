#!/bin/bash
# This is a shell script to extract module names from a pom.xml file and run each module
# shell script to extract module names from a pom.xml file and run each module in a new 
# vscode terminal instances in current window.

# Clear the terminal
clear

# Extract module names and store them in an array
modules=($(grep "<module>" pom.xml | sed 's/<module>\(.*\)<\/module>/\1/'))

if [[ "$TERM_PROGRAM" == "vscode" ]]; then
  echo "$TERM_PROGRAM is running the commands via apple terminal"
fi

# Function to run module
run_module() {
  local module="$1"
  local module_dir="$PWD/$module"
  
  if [[ "$module" == "models" || "$module" == "libraries" ]]; then
    echo "Running $module in $module_dir..."
    osascript -e "tell application \"Terminal\" to do script \"echo -n -e $module; $PWD/mvnw clean install -f $PWD/$module/pom.xml; exit\""
    sleep 10
    return
  fi
  
  if [[ -d "$module_dir" ]]; then
    echo "Running $module in $module_dir..."
    osascript << EOF
      tell application "Terminal"
        activate
        do script "cd ${module_dir} && $PWD/mvnw clean install -DskipTests && $PWD/mvnw spring-boot:run"
      end tell
EOF
  else
    echo "Directory $module_dir does not exist"
  fi
}

# Loop through the array and run each module
for module in "${modules[@]}"; do
  run_module "$module"
done
