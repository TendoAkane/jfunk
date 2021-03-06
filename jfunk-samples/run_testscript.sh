#!/bin/sh
#
# Copyright (c) 2015 mgm technology partners GmbH
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

if [ ! -d "./lib" ]; then
	echo Could not find required "lib" folder. Please run "mvn clean install" to generate it.
	exit
fi

. ./setenv.sh

echo "Using JAVA_HOME:   $JAVA_HOME"
echo "Using JAVA_OPTS:   $JAVA_OPTS"
echo "Using APP_OPTS:    $APP_OPTS"

$JAVA_HOME/bin/java $JAVA_OPTS $APP_OPTS -cp ./config -jar ./lib/jfunk-samples-$JFUNK_VERSION.jar -threadcount=4 $@

