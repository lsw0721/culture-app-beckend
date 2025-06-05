# -*- coding: utf-8 -*-
"""
Created on Wed Jun  4 23:21:10 2025

@author: pc
"""

import nbformat
from nbconvert.preprocessors import ExecutePreprocessor

def run_notebook(path):
    with open(path) as f:
        nb = nbformat.read(f, as_version=4)
    ep = ExecutePreprocessor(timeout=600, kernel_name='python3')
    ep.preprocess(nb, {'metadata': {'path': './'}})

run_notebook("모델_mysql연동.ipynb")