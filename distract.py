import csv
import json
import os
import random
import subprocess
import webbrowser
import time
from tkinter import messagebox
from datetime import datetime
from pathlib import Path

HOME = Path.home()
RESULTS_DIR = Path("results")
RESULTS_DIR.mkdir(exist_ok=True)

FITTS_C_DIR = Path("GoFittsCircle")
FITTS_R_DIR = Path("GoFittsRectangle")
TYPE_DIR = Path("TypingTest")
DBD_DIR = Path("DBD")

SD2_FITTS = "*.sd2" 
SD2_TYPE = "*.sd2"
DBD_URL = "http://localhost:8080"

def run_fitts(participant_id, task_index, fittsT): 

    if fittsT == "C":
        fittsDir = FITTS_C_DIR
    elif fittsT == "R":
        fittsDir = FITTS_R_DIR
        
    # Snapshot sd2 files before
    before = {p.name for p in fittsDir.glob(SD2_FITTS)}

    # Launch GoFitts
    subprocess.run(
        ["java", "-jar", "GoFitts.jar"],
        cwd=fittsDir,
        check=True
    )

    # If we want to parse data after each run then we can look through it, for now its just to reference data made by Fitt's module
    # Find new sd2 files
    after = {p.name for p in fittsDir.glob(SD2_FITTS)}
    new_files = list(after - before)
    if not new_files:
        raise RuntimeError("No new .sd2 file created by GoFitts.")

    # If multiple, pick the most recent by time
    sd2_paths = [fittsDir / name for name in new_files]
    sd2_path = max(sd2_paths, key=lambda p: p.stat().st_mtime)

    now = datetime.now().isoformat()

    return {
        "task_type": "fitts",
        "task_index": task_index,
        "start_time": now,
        "end_time": now,
        "config": {
            "sd2_file": sd2_path.name, # reference to the specific file name for data gathering specific to Fitt's Task
        } 
    }

def run_type(participant_id, task_index,condition_code: str = "C01", session_code: str = "S01", num_phrases: int = 5):

    # write cfg with participant / condition / session
    # we can also randomize some of these, I've set defaults in the inputs
    participant_code = participant_id   # or we can map to P01/P02
    write_typing_cfg(
        participant_code=participant_code,
        condition_code=condition_code,
        session_code=session_code,
        num_phrases=num_phrases,
        phrase_file="phrases2.txt",
        show_presented=True,
    )

    # Snapshot sd2 files before
    before = {p.name for p in TYPE_DIR.glob(SD2_TYPE)}

    # launch the Java app (participant runs block, then closes app)
    cmd = ["java", "-jar", "TypingTestExperiment.jar"]
    subprocess.run(cmd, cwd=TYPE_DIR, check=True)

    # If we want to parse data after each run then we can look through it, for now its just to reference data made by Typing module
    after = {p.name for p in TYPE_DIR.glob(SD2_TYPE)}
    new_files = list(after - before)
    if not new_files:
        raise RuntimeError("No new TypingTestExperiment .sd2 file created.")

    sd2_paths = [TYPE_DIR / name for name in new_files]
    sd2_path = max(sd2_paths, key=lambda p: p.stat().st_mtime)

    now = datetime.now().isoformat()

    return {
        "task_type": "typing",
        "task_index": task_index,
        "start_time": now,
        "end_time": now,
        "config": {
            "sd2_file": sd2_path.name, # reference to the specific file name for data gathering specific to Typing Task
            "condition_code": condition_code,
            "session_code": session_code,
        }
    }

# Writing the config file to start the typing test, with defaults
def write_typing_cfg(participant_code="P01",
                     condition_code="C01",
                     session_code="S01",
                     num_phrases=5,
                     phrase_file="phrases2.txt",
                     show_presented=True):
    cfg_path = TYPE_DIR / "TypingTestExperiment.cfg"
    text = f"""# =================================================
# Configuration parameters for TypingTestExperiment
# =================================================
# -----
# Parameter #1 (as per setup dialog)
{participant_code}
# -----
# Parameter #2 (as per setup dialog)
{condition_code}
# -----
# Parameter #3 (as per setup dialog)
{session_code}
# -----
# Parameter #4 (as per setup dialog)
{num_phrases}
# -----
# Parameter #5 (as per setup dialog)
{phrase_file}
# -----
# Parameter #6 (as per setup dialog)
{"true" if show_presented else "false"}
# --- end ---
"""
    cfg_path.write_text(text, encoding="utf-8")
    return cfg_path

def start_dbd_server():

    proc = subprocess.Popen(
        ["npm.cmd", "run", "serve"], 
        cwd=DBD_DIR,
        stdout=subprocess.DEVNULL,
        stderr=subprocess.DEVNULL
    )
    time.sleep(5)
    return proc

def run_dbd(participant_id, task_index):

    # Open browser
    webbrowser.open_new(DBD_URL)

    start_time = datetime.now().isoformat()
    input("Press ENTER here when they are finished... ")
    end_time = datetime.now().isoformat()
    return {
        "task_type": "dbd_skillcheck",
        "task_index": task_index,
        "start_time": start_time,
        "end_time": end_time,
        "config": {
            "url": DBD_URL
        }
    }

# Right now, its all on console but we can link it to the GUI
def run_tasks(id, end_time, auto, permissions):
    # Participant ID (1-12 or something)
    start_dbd_server()
    participant_id = id
    if not participant_id:
        print("No ID, aborting.")
        return

    # If we want to add more then we can
    tasks = ["type", "fittsR", "fittsC", "dbd"]
    task_num = 1

    # Create csv file for data gathering
    out_csv = RESULTS_DIR / f"{participant_id}_study.csv"
    fieldnames = [
        "participant_id", "task_type", "task_index",
        "start_time", "end_time",
        "auto_completed", "autocomplete_count",
        "config_json"
    ]
    new_file = not out_csv.exists()
    f = out_csv.open("a", newline="", encoding="utf-8")
    writer = csv.DictWriter(f, fieldnames=fieldnames)
    if new_file:
        writer.writeheader()
    # Auto-complete counter
    autocomplete_total = 0

    # while time.time() < end_time:
    #     t = random.choice(tasks)

    #     # Console command version, can attach to GUI properly
    #     print(f"Trial {task_num}: {t}")

    #     auto_completed = False
    #     autocomplete_count = 0

    #     if auto:
    #         # Right now, I have it set to ask for auto-complete at the start of task
    #         auto_completed = True
    #         autocomplete_count = 1
    #         autocomplete_total += 1
    #         now = datetime.now().isoformat()
    #         config = {"reason": "skipped via launcher auto-complete"}
    #         results = {}
    #     else:
    #     # If not auto-complete, proceed to task
    #         if t == "type":
    #             record = run_type(participant_id, task_num)
    #         elif t == "fitts":
    #             record = run_fitts(participant_id, task_num)
    #         elif t == "dbd":
    #             record = run_dbd(participant_id, task_num)
    #         else:
    #             raise ValueError(f"Unknown task {t}")

    #         config = record["config"]
    #         now_start = record["start_time"]
    #         now_end = record["end_time"]

    #     # If auto-completed, fabricate start/end times
    #     if auto_completed:
    #         now_start = now_end = datetime.now().isoformat()

    #     writer.writerow({
    #         "participant_id": participant_id,
    #         "task_type": t,
    #         "task_index": task_num,
    #         "start_time": now_start,
    #         "end_time": now_end,
    #         "auto_completed": auto_completed,
    #         "autocomplete_count": autocomplete_count,
    #         "config_json": json.dumps(config, ensure_ascii=False)
    #     })
    #     task_num += 1
    #     f.flush()
    #     # The tasks restart again after 5 minutes
    #     time.sleep(300)
    autocomplete_count = 0
    work_tasks(writer, tasks, f, participant_id, auto, end_time, task_num, 
               autocomplete_total, permissions, autocomplete_count)

    f.close()
    # print(f"\nExperiment complete. Total auto-completes used: {autocomplete_total}")
    # print(f"Results saved to: {out_csv}")

def work_tasks(writer, tasks, f, participant_id, auto, end, task_num, autocomplete_total, 
               permissions, autocomplete_count):
    t = random.choice(tasks)

    # Console command version, can attach to GUI properly
    print(f"Trial {task_num}: {t}")
    
    result = messagebox.askquestion("New Work Task Assigned!", "You have been assigned a new work task to complete.\n"+
                                         "\nAutocompleting will access your "+random.choice(permissions)+
                                         ".\n\nWould you like to autocomplete this task?"
                                         , icon=messagebox.WARNING)
    # result = messagebox.Message("New Work Task Assigned!", icon=messagebox.QUESTION)
    if result == 'yes':
        auto_completed = True
        auto = True
    else:
        auto_completed = False
        auto = False

    if time.time() < end:
        if auto:
            # Right now, I have it set to ask for auto-complete at the start of task
            # auto_completed = True
            autocomplete_count = 1
            autocomplete_total += 1
            now = datetime.now().isoformat()
            config = {"reason": "skipped via launcher auto-complete"}
            results = {}
        else:
        # If not auto-complete, proceed to task
            if t == "type":
                record = run_type(participant_id, task_num)
            elif t == "fittsR":
                record = run_fitts(participant_id, task_num, "R")
            elif t == "fittsC":
                record = run_fitts(participant_id, task_num, "C")
            elif t == "dbd":
                record = run_dbd(participant_id, task_num)
            else:
                raise ValueError(f"Unknown task {t}")

            config = record["config"]
            now_start = record["start_time"]
            now_end = record["end_time"]

        # If auto-completed, fabricate start/end times
        if auto_completed:
            now_start = now_end = datetime.now().isoformat()

        writer.writerow({
            "participant_id": participant_id,
            "task_type": t,
            "task_index": task_num,
            "start_time": now_start,
            "end_time": now_end,
            "auto_completed": auto_completed,
            "autocomplete_count": autocomplete_count,
            "config_json": json.dumps(config, ensure_ascii=False)
        })
        task_num += 1
        f.flush()
        work_tasks(writer, tasks, f, participant_id, auto_completed, end, task_num, 
                   autocomplete_total, permissions, autocomplete_count)
    else:
        print("Work day is over!")
        messagebox.showinfo("Clocked Out", "It looks like your work day is finished. Goodbye!")

# if __name__ == "__main__":
#     main()