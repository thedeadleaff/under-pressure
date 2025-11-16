import tkinter as tk

window = tk.Tk()
window.title("Hello World")
window.config(bg="black")

image = tk.PhotoImage(file="UnderPressure.png")

image_frame = tk.Frame(window, width=window.winfo_screenwidth(), height=window.winfo_screenheight(), bg="grey")
image_frame.pack(padx=5, pady=5, side=tk.BOTTOM)
display_image = image.subsample(1, 1)
# tk.Label(
#     image_frame,
#     text="Edited Image",
#     bg="grey",
#     fg="white",
# ).pack(padx=5, pady=5)
tk.Label(image_frame, image=display_image).pack(padx=5, pady=5)

frame = tk.Frame(window, width=window.winfo_screenwidth(), height=window.winfo_screenheight())
frame.config(bg="black")
frame.pack() # (padx=10, pady=10)

# nested_frame = tk.Frame(frame, width=800, height=200, bg="skyblue")
# nested_frame.pack(padx=10, pady=10)

# Start the event loop.
window.mainloop()
