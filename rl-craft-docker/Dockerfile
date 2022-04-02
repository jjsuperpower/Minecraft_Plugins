FROM openjdk:8

# ENV RL_CRAFT_MODS https://media.forgecdn.net/files/2935/323/RLCraft+Server+Pack+1.12.2+-+Beta+v2.8.2.zip
# ENV RL_CRAFT https://media.forgecdn.net/files/2935/316/RLCraft+1.12.2+-+Beta+v2.8.2.zip
# ENV MC_FORGE https://maven.minecraftforge.net/net/minecraftforge/forge/1.12.2-14.23.5.2860/forge-1.12.2-14.23.5.2860-installer.jar


ENV MC_INST_DIR /minecraft
ENV MEM=2G

WORKDIR /
COPY root /

RUN chmod +x init.sh

# RUN wget ${RL_CRAFT}
# RUN unzip RLCraft*.zip
# RUN rm RLCraft*.zip

# RUN wget ${RL_CRAFT_MODS}
# RUN unzip RLCraft*.zip
# RUN rm RLCraft*.zip

# RUN wget ${MC_FORGE}
# RUN mv *.jar install.jar
# RUN echo eula=true > eula.txt

# RUN java -jar install.jar --installServer
# RUN rm install*
# RUN mv forge*.jar server.jar

EXPOSE 25565/tcp

RUN ls -l

CMD ["bash /init.sh"]
