set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 6]);

x=[1, 2, 3, 4, 5];
y1 = [429219	318733	237309	178946	136629];
y2 = [5	17	75	79	36979];
y3 = [5	92.1	165	219	142];

p1= semilogy(x, y1, '-kv');
hold on;
p2 = semilogy(x, y2, '-ks');
hold on;
p3 = semilogy(x, y3, '-kd');

xlabel('k');
ylabel('community size');

axis([0.5 5.5 0.0 1000000]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'4','5','6','7','8'});
leg=legend('Global','Local','ACQ', 2);
set(leg,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);


