#!/bin/bash

# get url for rl-craft install
get_rlcraft_url()   {
    local rlcraft_base_url="https://media.forgecdn.net/files"
    local forge12_2="https://maven.minecraftforge.net/net/minecraftforge/forge/1.12.2-14.23.5.2860/forge-1.12.2-14.23.5.2860-installer.jar"
    local forge11_2="https://maven.minecraftforge.net/net/minecraftforge/forge/1.11.2-13.20.1.2588/forge-1.11.2-13.20.1.2588-installer.jar"

    case $1 in 
        "2.9")
            RL_CRAFT_URL="${rlcraft_base_url}/3575/916/RLCraft+Server+Pack+1.12.2+-+Release+v2.9.zip"
            MC_FORGE=${forge12_2}
        ;;

        "2.8.2")
            RL_CRAFT_URL="${rlcraft_base_url}/2935/323/RLCraft+Server+Pack+1.12.2+-+Beta+v2.8.2.zip"
            MC_FORGE=${forge12_2}
        ;;

        "2.8.1")
            RL_CRAFT_URL="${rlcraft_base_url}/2836/138/RLCraft+Server+Pack+1.12.2+-+Beta+v2.8.1.zip"
            MC_FORGE=${forge12_2}
        ;;

        "2.8")
            RL_CRAFT_URL="${rlcraft_base_url}/2833/1/RLCraft+Server+Pack+1.12.2+-+Beta+v2.8.zip"
            MC_FORGE=${forge12_2}
        ;;

        "2.7")
            RL_CRAFT_URL="${rlcraft_base_url}/2800/990/RLCraft+Server+Pack+1.12.2+-+Beta+v2.7.zip"
            MC_FORGE=${forge12_2}
        ;;

        "2.6.3")
            RL_CRAFT_URL="${rlcraft_base_url}/2791/783/RLCraft+Server+Pack+1.12.2+-+Beta+v2.6.3.zip"
            MC_FORGE=${forge12_2}
        ;;

        "2.5")
            RL_CRAFT_URL="${rlcraft_base_url}/2780/296/RLCraft+Server+Pack+1.12.2+-+Beta+v2.5.zip"
            MC_FORGE=${forge12_2}
        ;;

        "2.4")
            RL_CRAFT_URL=""
            MC_FORGE=${forge12_2}
        ;;

        "2.3")
            RL_CRAFT_URL=""
            MC_FORGE=${forge12_2}
        ;;

        "2.2")
            RL_CRAFT_URL=""
            MC_FORGE=${forge12_2}
        ;;

        "2.1")
            RL_CRAFT_URL=""
            MC_FORGE=${forge12_2}
        ;;

        "1.4")
            RL_CRAFT_URL="${rlcraft_base_url}/2533/561/RLCraft+Server+Pack+1.11.2+-+Beta+v1.4.zip"
            MC_FORGE=${forge11_2}
        ;;

        "1.3")
            RL_CRAFT_URL="${rlcraft_base_url}/2530/772/RLCraft+Server+Pack+1.11.2+-+Beta+v1.3.zip"
            MC_FORGE=${forge11_2}
        ;;

        "1.2")
            RL_CRAFT_URL="${rlcraft_base_url}/2525/512/RLCraft+Server+Pack+1.11.2+-+Beta+v1.2.zip"
            MC_FORGE=${forge11_2}
        ;;

        "1.1")
            RL_CRAFT_URL="${rlcraft_base_url}/2520/485/RLCraft+Server+Pack+1.11.2+-+Beta+v1.1.zip"
            MC_FORGE=${forge11_2}
        ;;

        "1.0")
            RL_CRAFT_URL=""
            MC_FORGE=${forge11_2}
        ;;
    esac
}
