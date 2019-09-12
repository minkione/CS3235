FROM node:8.16.1-jessie

# install noble
RUN apt-get update \
    && apt-get install -y \
    bluetooth bluez libbluetooth-dev libudev-dev
# install gattacker and dependencies
RUN npm install bento noble gattacker

CMD /bin/bash

